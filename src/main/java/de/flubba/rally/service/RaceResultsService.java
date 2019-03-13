package de.flubba.rally.service;

import de.flubba.rally.entity.Lap;
import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.Sponsor;
import de.flubba.rally.entity.repository.LapRepository;
import de.flubba.rally.entity.repository.RunnerRepository;
import de.flubba.rally.entity.repository.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RaceResultsService {
    private final RunnerRepository runnerRepository;
    private final LapRepository lapRepository;
    private final SponsorRepository sponsorRepository;

    @Autowired
    public RaceResultsService(RunnerRepository runnerRepository, LapRepository lapRepository, SponsorRepository sponsorRepository) {
        this.runnerRepository = runnerRepository;
        this.lapRepository = lapRepository;
        this.sponsorRepository = sponsorRepository;
    }

    public void generateResults() {
        runnerRepository.findAll().forEach(runner -> {
            runner.setNumberOfLapsRun(lapRepository.findByRunner(runner).size());
            runnerRepository.saveAndFlush(runner);

            BigDecimal runnersTotalDonation = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
            List<Sponsor> sponsors = sponsorRepository.findByRunner(runner);
            for (Sponsor sponsor : sponsors) {
                BigDecimal oneTimeDonation = getOneTimeDonation(sponsor);
                BigDecimal lapDonation = getLapDonation(sponsor, runner);
                BigDecimal sponsorsTotalDonation = lapDonation.add(oneTimeDonation);
                sponsorsTotalDonation = sponsorsTotalDonation.setScale(2, RoundingMode.HALF_UP);
                sponsor.setTotalDonation(sponsorsTotalDonation);
                sponsorRepository.saveAndFlush(sponsor);
                runnersTotalDonation = runnersTotalDonation.add(sponsorsTotalDonation);
            }
            runner.setDonations(runnersTotalDonation);
            runner.setNumberOfSponsors(sponsors.size());
            runnerRepository.saveAndFlush(runner);

            List<Lap> laps = lapRepository.findByRunner(runner);
            double totalTime = 0;
            for (Lap lap : laps) {
                totalTime += lap.getDuration();
            }
            long lapsWithTime = (laps.size() - 1);
            if (lapsWithTime != 0) {
                BigDecimal average = new BigDecimal(totalTime).divide(new BigDecimal((laps.size() - 1) * 1000), 3, RoundingMode.HALF_UP);
                runner.setAverage(average);
                runnerRepository.saveAndFlush(runner);
            }
        });
    }

    private static BigDecimal getLapDonation(Sponsor sponsor, Runner runner) {
        Long bonusLaps = runner.getBonusLaps();
        if (bonusLaps == null) {
            bonusLaps = 0L;
        }
        BigDecimal laps = new BigDecimal(runner.getNumberOfLapsRun() + bonusLaps);

        BigDecimal perLapDonation = sponsor.getPerLapDonation();
        BigDecimal lapDonation = new BigDecimal(0);
        if (perLapDonation != null) {
            lapDonation = perLapDonation.multiply(laps);
        }
        return lapDonation;
    }

    private static BigDecimal getOneTimeDonation(Sponsor sponsor) {
        BigDecimal oneTimeDonation = sponsor.getOneTimeDonation();
        if (oneTimeDonation == null) {
            oneTimeDonation = new BigDecimal(0);
        }
        return oneTimeDonation;
    }
}
