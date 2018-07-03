package framework;

import java.util.Locale;

/**
 * 错误/异常响应。
 * <p>
 * 一般在生产环境下响应，开发环境触发默认流程。
 *
 * @author mrzhqiang
 */
public final class ErrorResponse {

  private static final String APP_PACKAGES_REGEX =
      "^(controllers|models|util|framework|core|service|rest).*";

  public int httpStatus;
  public int code;
  public String message;
  public String developerMessage;
  public String moreInfo;

  private ErrorResponse() {
    this.httpStatus = 500;
    this.code = -1;
    this.message = "A unknown error occurred.";
    this.developerMessage = "Reference [moreInfo].";
    this.moreInfo = "http://developer.randall.top";
  }

  public static ErrorResponse unknownError(int code) {
    ErrorResponse error = new ErrorResponse();
    error.code = code;
    return error;
  }

  public static ErrorResponse clientError(int httpStatus, int code, String message) {
    ErrorResponse error = new ErrorResponse();
    error.httpStatus = httpStatus;
    error.code = code;
    error.message = message;
    return error;
  }

  public static ErrorResponse serverError(Throwable throwable) {
    ErrorResponse error = new ErrorResponse();
    error.message = "A server error occurred.";
    error.developerMessage = findMessageFromStackTrace(throwable);
    return error;
  }

  private static String findMessageFromStackTrace(Throwable throwable) {
    if (throwable == null) {
      return "Not content.";
    }

    for (StackTraceElement trace : throwable.getStackTrace()) {
      String className = trace.getClassName();
      String packageName = className.substring(0, className.indexOf("."));
      if (packageName.matches(APP_PACKAGES_REGEX)) {
        return String.format(Locale.getDefault(),
            "Exception: %s, StackTrace: %s",
            throwable, trace);
      }
    }
    return throwable.toString();
  }
}
