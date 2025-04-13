package io.cdap.wrangler.executor;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.parser.ColumnName;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

  @Test
  public void testAggregateStats() throws Exception {
    // Create sample rows with test data
    List<Row> rows = Arrays.asList(
      new Row("data_size", "1MB").add("response_time", "500ms"),
      new Row("data_size", "2MB").add("response_time", "1500ms"),
      new Row("data_size", "512KB").add("response_time", "1000ms")
    );

    // Build a simple map of tokens for the directive's arguments.
    Map<String, Object> argMap = new HashMap<>();
    argMap.put("sizeColumn", new ColumnName("data_size"));
    argMap.put("timeColumn", new ColumnName("response_time"));
    argMap.put("totalSizeCol", new ColumnName("total_size_mb"));
    argMap.put("totalTimeCol", new ColumnName("total_time_sec"));

    // Create the Arguments object using our dummy class.
    Arguments args = new Arguments(argMap);

    // Initialize and execute the directive
    AggregateStats directive = new AggregateStats();
    directive.initialize(args);
    List<Row> results = directive.execute(rows, null);

    // Validate the result
    assertEquals(1, results.size());
    Row result = results.get(0);

    double expectedMB = (1 * 1024 * 1024 + 2 * 1024 * 1024 + 512 * 1024) / (1024.0 * 1024);
    double expectedSec = (500 + 1500 + 1000) / 1000.0;

    assertEquals(expectedMB, (double) result.getValue("total_size_mb"), 0.001);
    assertEquals(expectedSec, (double) result.getValue("total_time_sec"), 0.001);
  }
}
