package de.flubba.rally.rest;

import de.flubba.rally.LapBroadcaster;
import de.flubba.rally.entity.Lap;
import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.TagAssignment;
import de.flubba.rally.entity.repository.LapRepository;
import de.flubba.rally.entity.repository.RunnerRepository;
import de.flubba.rally.entity.repository.TagAssignmentRepository;
import de.flubba.rally.rest.dto.RunnerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TagController {

    private static final Logger log = LoggerFactory.getLogger(TagController.class);

    @Value("${de.flubba.rally.min-lap-duration}")
    private Long minimumLapDuration;

    private final TagAssignmentRepository tagAssignmentRepository;
    private final RunnerRepository runnerRepository;
    private final LapRepository lapRepository;

    public TagController(TagAssignmentRepository tagAssignmentRepository, RunnerRepository runnerRepository, LapRepository lapRepository) {
        this.tagAssignmentRepository = tagAssignmentRepository;
        this.runnerRepository = runnerRepository;
        this.lapRepository = lapRepository;
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<String> handleTagNotFound(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({TagAlreadyAssignedException.class,
            RunnerAlreadyAssignedException.class,
            NoRunnerFoundException.class,
            LapTooShortException.class})
    public ResponseEntity<String> handleExistingTag(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    private static final class LapTooShortException extends Exception {
        private LapTooShortException(long minimumLapDuration, long duration) {
            super(String.format("Lap too short! Was only %s seconds. Minimum lap duration is %s seconds.", duration, minimumLapDuration));
        }
    }

    private static final class NoRunnerFoundException extends Exception {
        private NoRunnerFoundException(TagAssignment tagAssignment) {
            super(String.format("No runner with id %s found for counting laps.", tagAssignment.getRunnerId()));
            log.debug("No runner with id {} found for counting laps.", tagAssignment.getTagId());
        }
    }

    private static final class TagAlreadyAssignedException extends Exception {
        private TagAlreadyAssignedException(TagAssignment tagAssignment) {
            super(String.format("Tag %s is already assigned to runner %s.", tagAssignment.getTagId(), tagAssignment.getRunnerId()));
            log.debug("Tag {} not assigned because it is already assigned.", tagAssignment.getTagId());
        }
    }

    private static final class RunnerAlreadyAssignedException extends Exception {
        private RunnerAlreadyAssignedException(TagAssignment tagAssignment) {
            super(String.format("Runner %s is already assigned to tag %s.", tagAssignment.getRunnerId(), tagAssignment.getTagId()));
            log.debug("Runner {} not assigned because it is already assigned.", tagAssignment.getRunnerId());
        }
    }

    private static final class TagNotFoundException extends Exception {
        private TagNotFoundException(String tagId) {
            super(String.format("Tag %s is not registered.", tagId));
        }
    }

    @RequestMapping(value = "/countLap", method = RequestMethod.POST)
    public RunnerDto countLap(String tagId) throws TagNotFoundException, NoRunnerFoundException, LapTooShortException {
        TagAssignment tagAssignment = getTagAssignment(tagId);
        Runner runner = findRunner(tagAssignment);
        long duration = getLapDuration(runner);
        saveNewLap(runner, duration);
        LapBroadcaster.broadcast(runner, duration);
        return createRunnerDto(runner);
    }

    private static RunnerDto createRunnerDto(Runner runner) {
        return new RunnerDto(runner.getId(), runner.getName());
    }

    private void saveNewLap(Runner runner, long duration) {
        Lap lap = new Lap();
        lap.setTime(System.currentTimeMillis());
        lap.setRunner(runner);
        lap.setDuration(duration);
        lapRepository.saveAndFlush(lap);
    }

    private long getLapDuration(Runner runner) throws LapTooShortException {
        long duration = 0;
        long currentTime = System.currentTimeMillis();
        Lap lastLap = lapRepository.findLastLap(runner);
        if (lastLap != null) {
            duration = currentTime - lastLap.getTime();
            if (duration < minimumLapDuration * 1000) {
                throw new LapTooShortException(minimumLapDuration, (duration / 1000));
            }
        }
        return duration;
    }

    private Runner findRunner(TagAssignment tagAssignment) throws NoRunnerFoundException {
        Runner runner = runnerRepository.findOneById(tagAssignment.getRunnerId());
        if (runner == null) {
            throw new NoRunnerFoundException(tagAssignment);
        }
        return runner;
    }

    @RequestMapping(value = "/getTagAssignment")
    public TagAssignment getTagAssignment(String tagId) throws TagNotFoundException {
        TagAssignment tagAssignment = tagAssignmentRepository.findOneByTagId(tagId);
        if (tagAssignment == null) {
            throw new TagNotFoundException(tagId);
        }
        return tagAssignment;
    }

    @RequestMapping(value = "/setTagAssignment", method = RequestMethod.POST)
    public String setTagAssignment(@RequestParam(defaultValue = "false") boolean overwrite,
                                   String tagId,
                                   Long runnerId) throws RunnerAlreadyAssignedException,
            TagAlreadyAssignedException {
        TagAssignment tagAssignment = new TagAssignment();
        tagAssignment.setTagId(tagId);
        tagAssignment.setRunnerId(runnerId);

        if (overwrite) {
            deleteExistingAssignment(tagAssignment);
        }
        checkIfTagAssignmentExists(tagAssignment);

        log.debug("Trying to assingn runner id {} to tag id {}", runnerId, tagId);
        tagAssignmentRepository.saveAndFlush(tagAssignment);

        log.debug("Assignment of runner id {} to tag id {} successful.", runnerId, tagId);

        return String.format("Tag %s assigned to runner %s.", tagAssignment.getTagId(), tagAssignment.getRunnerId());
    }

    private void deleteExistingAssignment(TagAssignment tagAssignment) {
        log.debug("Deleting existing assignments for {} and runner {}.", tagAssignment.getTagId(), tagAssignment.getRunnerId());
        TagAssignment existingAssignment = tagAssignmentRepository.findOneByRunnerId(tagAssignment.getRunnerId());
        if (existingAssignment != null) {
            tagAssignmentRepository.delete(existingAssignment);
        }
        existingAssignment = tagAssignmentRepository.findOneByTagId(tagAssignment.getTagId());
        if (existingAssignment != null) {
            tagAssignmentRepository.delete(existingAssignment);
        }
    }

    private void checkIfTagAssignmentExists(TagAssignment tagAssignment) throws RunnerAlreadyAssignedException,
            TagAlreadyAssignedException {
        TagAssignment existingAssignment = tagAssignmentRepository.findOneByRunnerId(tagAssignment.getRunnerId());
        if (existingAssignment != null) {
            throw new RunnerAlreadyAssignedException(existingAssignment);
        }
        existingAssignment = tagAssignmentRepository.findOneByTagId(tagAssignment.getTagId());
        if (existingAssignment != null) {
            throw new TagAlreadyAssignedException(existingAssignment);
        }
    }
}
