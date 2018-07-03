package framework;

import core.exception.ApplicationException;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

public final class ErrorResponse {

  public int httpStatus;
  public int code;
  public String property;
  public String message;
  public String developerMessage;
  public String moreInfo;

  private ErrorResponse() {
    // this must be invoke static method
  }

  public static ErrorResponse unknownError(Throwable e) {
    ErrorResponse err = new ErrorResponse();
    err.httpStatus = Http.Status.INTERNAL_SERVER_ERROR;
    err.code = 1;
    err.message = "A server error occurred";
    err.developerMessage = stackTraceStringFromAppCode(e);
    err.moreInfo = "http://developer.touchair.cn/uppc";
    return err;
  }

  public static ErrorResponse notFound() {
    ErrorResponse err = new ErrorResponse();
    err.httpStatus = Http.Status.NOT_FOUND;
    err.code = 4;
    err.message = "resource not found";
    err.developerMessage = "";
    err.moreInfo = "http://developer.touchair.cn/uppc";
    return err;
  }

  public static ErrorResponse appException(ApplicationException ae) {
    ErrorResponse err = new ErrorResponse();
    err.httpStatus = ae.getHttpStatus();
    err.code = ae.errorCode;
    err.message = ae.getMessage();
    err.developerMessage = ae.getDeveloperMessage();
    if (err.developerMessage == null) {
      err.developerMessage = stackTraceStringFromAppCode(ae);
    }
    err.moreInfo = "http://developer.touchair.cn/uppc";
    return err;
  }

  public static ErrorResponse badRequest(IllegalStateException e) {
    ErrorResponse err = new ErrorResponse();
    err.httpStatus = Http.Status.BAD_REQUEST;
    err.code = 1; // FIXME
    err.message = e.getMessage();
    err.developerMessage = stackTraceStringFromAppCode(e);
    err.moreInfo = "http://developer.touchair.cn/uppc";
    return err;
  }

  public static ErrorResponse badRequest(IllegalArgumentException e) {
    ErrorResponse err = new ErrorResponse();
    err.httpStatus = Http.Status.BAD_REQUEST;
    err.code = 2; // FIXME
    err.message = e.getMessage();
    err.developerMessage = stackTraceStringFromAppCode(e);
    err.moreInfo = "http://developer.touchair.cn/uppc";
    return err;
  }

  public static ErrorResponse unauthenticated(String message, String developerMessage) {
    ErrorResponse err = new ErrorResponse();
    err.httpStatus = Http.Status.UNAUTHORIZED;
    err.code = 2;
    err.message = message;
    err.developerMessage = developerMessage;
    err.moreInfo = "http://developer.touchair.cn/uppc";
    return err;
  }

  public static ErrorResponse forbidden(String message, String developerMessage) {
    ErrorResponse err = new ErrorResponse();
    err.httpStatus = Http.Status.FORBIDDEN;
    err.code = 3;
    err.message = message;
    err.developerMessage = developerMessage;
    err.moreInfo = "http://developer.touchair.cn/uppc";
    return err;
  }

  public Result toJsonResult() {
    return Results.status(httpStatus, Json.toJson(this));
  }

  private static final String appPackagesRegex = "^[(controllers)|(core)|(database)|(util)|(filters)|" +
      "(framework)|(model)|(service)]\\w+";

  private static String stackTraceStringFromAppCode(Throwable e) {
    //StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
      String className = element.getClassName();
      String packageName = className.substring(0, className.lastIndexOf("."));

      if (packageName.matches(appPackagesRegex)) {
        return "Exception: " + e.getMessage() + " at line " + element.getLineNumber() +
            " of file: " + element.getFileName() + ", in method:" + className + "." +
            element.getMethodName() + "()";
      }
    }
    return e.getLocalizedMessage();
  }
}
