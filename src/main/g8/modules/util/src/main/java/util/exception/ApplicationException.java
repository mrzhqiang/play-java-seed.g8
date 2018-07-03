package core.exception;

import java.util.HashMap;
import java.util.Map;

public class ApplicationException extends RuntimeException {

  private final static Map<Integer, String> defaultErrorMessages = new HashMap<>();

  /** 服务器内部错误。 */
  public static final int SERVER_ERROR = 10000;
  /** 序列号为 Null 或 Empty。 */
  public static final int USER_ID_EMPTY = 10001;
  /** 序列号不合法。 */
  public static final int USER_ID_INVALID = 10002;
  /** 不存在的序列号。 */
  public static final int USER_ID_NOT_EXISTS = 10003;
  /** 时间字符串为 Null 或 Empty。 */
  public static final int TIME_EMPTY = 11001;
  /** 时间字符串无法转换为 UTC Date。 */
  public static final int TIME_INVALID = 11002;
  /** 起始时间应当在结束时间之前。 */
  public static final int TIME_BEFORE = 11003;
  /** 页面序号不合法。 */
  public static final int PAGE_INVALID = 11004;
  /** 页面序号超出范围（1 —— total - (page -1) * size）。 */
  public static final int PAGE_WITHOUT = 11005;
  /** 分页大小不合法（10 —— Integer.MAX）。 */
  public static final int SIZE_INVALID = 11006;
  /** 纬度不合法（-90 —— 90）。 */
  public static final int LAT_INVALID = 11007;
  /** 经度不合法（-180 —— 180）。 */
  public static final int LON_INVALID = 11008;
  /** 上纬度应当大于下纬度。 */
  public static final int LAT_GREATER_THAN = 11009;
  /** 左经度应当小于右经度。 */
  public static final int LON_LESS_THAN = 11010;
  /** 秒钟范围不合法（0 —— Long.MAX）。 */
  public static final int SECONDS_INVALID = 11011;

  static {
    defaultErrorMessages.put(SERVER_ERROR, "The error is server bug.");
    defaultErrorMessages.put(USER_ID_EMPTY, "The user ID must be not null and not empty.");
    defaultErrorMessages.put(USER_ID_INVALID, "The user ID invalid that should is serial number.");
    defaultErrorMessages.put(USER_ID_NOT_EXISTS, "The user ID not exists in server.");
    defaultErrorMessages.put(TIME_EMPTY, "The time string must be not null and not empty.");
    defaultErrorMessages.put(TIME_INVALID, "The time string invalid that should is UTC format.");
    defaultErrorMessages.put(TIME_BEFORE, "The start time must be before the end time.");
    defaultErrorMessages.put(PAGE_INVALID, "The page value must be greater than 0.");
    defaultErrorMessages.put(PAGE_WITHOUT, "The page value has out of range.");
    defaultErrorMessages.put(SIZE_INVALID, "The size value must be greater than 9.");
    defaultErrorMessages.put(LAT_INVALID, "The latitude value in [-90, 90].");
    defaultErrorMessages.put(LON_INVALID, "The longitude value in [-180, 180].");
    defaultErrorMessages.put(LAT_GREATER_THAN,
        "The upper left latitude must be greater than the lower right latitude.");
    defaultErrorMessages.put(LON_LESS_THAN,
        "The upper left longitude must be less than the lower right longitude.");
    defaultErrorMessages.put(SECONDS_INVALID,
        "The seconds value must be integer and greater than 0.");
  }

  public int errorCode;
  public String property;
  public String developerMessage;

  public ApplicationException(int errorCode) {
    this(errorCode, null);
  }

  public ApplicationException(String message) {
    this(SERVER_ERROR, message);
  }

  public ApplicationException(Throwable cause) {
    this(SERVER_ERROR, null, cause);
  }

  public ApplicationException(int errorCode, String message) {
    this(errorCode, message, null);
  }

  public ApplicationException(int errorCode, String message, Throwable cause) {
    this(errorCode, message, cause, "");
  }

  public ApplicationException(int errorCode, String message, Throwable cause, String property) {
    super(defaultErrorMessages.getOrDefault(errorCode, message));
    if (cause != null) {
      initCause(cause);
    }
    this.errorCode = errorCode;
    this.property = property;
  }

  public int getHttpStatus() {
    return 500;
  }

  public String getDeveloperMessage() {
    return developerMessage;
  }
}
