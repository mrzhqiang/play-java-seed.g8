package util;

import java.util.Date;
import org.junit.Test;

import static org.junit.Assert.*;

public final class HttpDateTest {

  @Test
  public void parse() {
    String emptySource = "";
    assertNull(HttpDate.parse(emptySource));

    // 标准的 GMT 时间
    String standardSource = "Thu, 05 Jul 2018 12:13:57 GMT";
    Date standardDate = HttpDate.parse(standardSource);
    assertNotNull(standardDate);

    // 干扰字段，前缀（无法解析）
    String prefixSource = "start Thu, 05 Jul 2018 12:13:57 GMT";
    Date prefixDate = HttpDate.parse(prefixSource);
    assertNull(prefixDate);

    // 干扰字段，后缀（标准时间是严格的，有后缀会切换到其他格式，不影响解析）
    String suffixSource = "Thu, 05 Jul 2018 12:13:57 GMT end";
    Date suffixDate = HttpDate.parse(suffixSource);
    assertNotNull(suffixDate);

    // 缺斤少两（数字增加和减少，影响解析的精度）
    String lackNumberSource = "Thu, 05001 Jul 218 12:13:57 GMT";
    Date lackNumberDate = HttpDate.parse(lackNumberSource);
    assertNotNull(lackNumberDate);

    // 缺斤少两（字母缺少和增加，无法解析）
    String lackLetterSource = "Thud, 05 Jl 2018 12:13:57 GMT";
    Date lackLetterDate = HttpDate.parse(lackLetterSource);
    assertNull(lackLetterDate);
  }

  @Test
  public void format() {
    // Thu, 05 Jul 2018 14:49:24 GMT
    System.out.println(HttpDate.format(new Date()));
  }
}