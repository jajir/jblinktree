package com.coroptis.jblinktree.performance;

import java.io.IOException;
import java.util.Collection;

import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.openjdk.jmh.util.Statistics;

import com.coroptis.jblinktree.performance.tool.MergeTestResults;

public class MapTestRunner {

    private static Options getOptions(final Class<?> clazz) {
	return new OptionsBuilder()
		.include(clazz.getSimpleName())
		.threads(100)
		.measurementIterations(10)
		.verbosity(VerboseMode.NORMAL)
		.forks(5)
		.measurementIterations(10)
		.result("./target/result-" + clazz.getSimpleName() + ".csv")
		.resultFormat(ResultFormatType.CSV)
		.jvmArgsAppend("-server", "-XX:+AggressiveOpts", "-dsa",
			"-Xbatch", "-Xmx1024m").build();
    }

    private static void showResults(final Collection<RunResult> runResults) {
	System.out.println("");
	System.out
		.println("------------------------------------------------------------");
	System.out.println("");
	for (final RunResult runResult : runResults) {
	    Result<?> result = runResult.getPrimaryResult();
	    Statistics stats = result.getStatistics();
	    System.out.println(stats.toString());
	    BenchmarkResult benchmarkResult = runResult.getAggregatedResult();
	    System.out.println(benchmarkResult);
	}
    }

    public static void main(String[] args) throws RunnerException, IOException {
	showResults(new Runner(getOptions(MapTestJbTreeMap.class)).run());
	showResults(new Runner(getOptions(MapTestConcurrentHashMap.class))
		.run());
	showResults(new Runner(getOptions(MapTestSynchronizedHashMap.class))
		.run());
	showResults(new Runner(getOptions(MapTestSynchronizedTreeMap.class))
		.run());
	new MergeTestResults("./target/").merge();
    }

}
