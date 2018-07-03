package framework;

import com.google.common.collect.Lists;
import filters.AccessLogFilter;
import filters.VersionFilter;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.Environment;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;

/**
 * This class configures filters that run on every request. This
 * class is queried by Play to get a list of filters.
 * <p>
 * Play will automatically use filters from any class called
 * <code>Filters</code> that is placed the root package. You can load filters
 * from a different class by adding a `play.http.filters` setting to
 * the <code>application.conf</code> configuration file.
 */
@Singleton
public final class Filters implements HttpFilters {

  private final Environment env;
  private final List<EssentialFilter> filters;

  @Inject
  public Filters(Environment env, VersionFilter versionFilter, AccessLogFilter accessLogFilter) {
    this.env = env;
    this.filters = Lists.newArrayList(versionFilter, accessLogFilter);
  }

  @Override public List<EssentialFilter> getFilters() {
    if (env.isTest()) {
      return filters;
    }
    return Collections.emptyList();
  }
}
