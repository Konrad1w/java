package uj.wmii.pwj.anns;

public class MyBeautifulTestSuite {

    @MyTest
    public void testSoemthing() {
        System.out.println("I'm testing something!");
    }

    @MyTest(params = {"a param TYPE: string", "b param TYPE: string", "c param. Long, long C param. TYPE: string"})
    public void testWithParam(String param) {
        System.out.printf("I was invoked with parameter: %s\n", param);
    }

    public void notATest() {
        System.out.println("I'm not a test.");
    }

    @MyTest
    public void imFailue() {
        System.out.println("I AM EVIL.");
        throw new NullPointerException();
    }

    @MyTest(params = {"10 TYPE: string EXPECTED: 10 TYPE: int"})
    public int testWithIntParamReturnedPassed(String s) {
        return 10;
    }

    @MyTest(params = {"10 TYPE: string TYPE: int"})
    public int testWithNoExpectedValue(String s) {
        return 10;
    }

    @MyTest(params = {"10 TYPE: string EXPECTED: 10"})
    public int testWithNoTypeValue(String s) {
        return 10;
    }

    @MyTest(params = {"10 TYPE: string EXPECTED: 10 TYPE: int"})
    public String testWithIntParamReturnedFailed(String s) {
        return "10";
    }

}
