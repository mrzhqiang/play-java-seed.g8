package framework;

import framework.filters.AccessLogFilter;
import framework.filters.VersionFilter;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.Environment;
import play.http.DefaultHttpFilters;
import play.mvc.EssentialFilter;

/**
 * 自定义过滤器。
 * <p>
 * 参考：<a href="https://www.playframework.com/documentation/2.6.x/JavaHttpFilters">Filter 文档。</a>
 *
 * @author mrzhqiang
 */
@Singleton
public final class Filters extends DefaultHttpFilters {

  private final Environment env;

  @Inject
  public Filters(VersionFilter versionFilter, AccessLogFilter accessLogFilter, Environment env) {
    super(versionFilter, accessLogFilter);
    this.env = env;
  }

  @Override public List<EssentialFilter> getFilters() {
    if (env.isTest()) {
      return super.getFilters();
    }
    return Collections.emptyList();
  }
}
