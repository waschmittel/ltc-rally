package de.flubba.rally;

/**
 * Created by FlassakD on 24.08.2016.
 */
// @ControllerAdvice
public class ExceptionConfiguration {
    // public static class HttpNotFoundException extends RuntimeException {
    // public HttpNotFoundException(String message) {
    // super(message);
    // }
    // }
    //
    // private static ModelAndView getModelAndView(Exception e, HttpStatus status) {
    // ModelAndView modelAndView = new ModelAndView();
    // modelAndView.addObject("errorCode", status.value());
    // modelAndView.addObject("errorMessage", e.getClass().getTypeName() + ": " + e.getMessage());
    // modelAndView.setViewName("error");
    // return modelAndView;
    // }
    //
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // @ExceptionHandler(Exception.class)
    // public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
    // return getModelAndView(e, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    //
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    // @ExceptionHandler(HttpNotFoundException.class)
    // public ModelAndView notFoundErrorHandler(HttpServletRequest request, Exception e) {
    // return getModelAndView(e, HttpStatus.NOT_FOUND);
    // }
}
