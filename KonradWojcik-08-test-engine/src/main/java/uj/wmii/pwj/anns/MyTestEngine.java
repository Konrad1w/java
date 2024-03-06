package uj.wmii.pwj.anns;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyTestEngine {
    private final String className;
    int successCount = 0;
    int failCount = 0;
    int errorCount = 0;

    public MyTestEngine(String className) {
        this.className = className;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please specify test class name");
            System.exit(-1);
        }
        String className = args[0].trim();
        System.out.printf("Testing class: %s\n", className);
        MyTestEngine engine = new MyTestEngine(className);
        engine.runTests();
    }

    private static Object parse(String value) {
        var splited = value.split(" TYPE: ");
        return switch (splited[1].trim()) {
            case "int" -> Integer.parseInt(splited[0]);
            case "double" -> Double.parseDouble(splited[0]);
            case "boolean" -> Boolean.parseBoolean(splited[0]);
            case "string" -> splited[0];
            default -> null;
        };
    }

    private static List<Method> getTestMethods(Object unit) {
        Method[] methods = unit.getClass().getDeclaredMethods();
        return Arrays.stream(methods).filter(
                m -> m.getAnnotation(MyTest.class) != null).collect(Collectors.toList());
    }

    private static Object getObject(String className) {
        try {
            Class<?> unitClass = Class.forName(className);
            return unitClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return new Object();
        }
    }

    public void runTests() {
        final Object unit = getObject(className);
        List<Method> testMethods = getTestMethods(unit);
        printBeforeExecutionTests(testMethods);
        for (Method m : testMethods) {
            launchSingleMethod(m, unit);
        }
        System.out.printf("Engine launched %d tests.\n", testMethods.size());
        System.out.printf("%d of them passed, %d failed, %d with error.\n", successCount, failCount, errorCount);
    }

    private void launchSingleMethod(Method m, Object unit) {
        String[] params = m.getAnnotation(MyTest.class).params();
        System.out.println("Testing test method: " + m.getName());
        if (params.length == 0) {
            try {
                m.invoke(unit);
                successCount++;
                System.out.println("Tested method: " + m.getName() + " test successful.\n");
            } catch (Exception e) {
                errorCount++;
                System.out.println("Tested method: " + m.getName() + " test error.\n");
            }
        } else {
            for (String param : params) {
                String[] functionParam = param.split(" EXPECTED: ");
                try {
                    Object functionResult;
                    if (functionParam.length == 0)
                        functionResult = m.invoke(unit);
                    else
                        functionResult = m.invoke(unit, parse(functionParam[0]));
                    if (functionParam.length == 1 && !m.getReturnType().getName().equals("void")) {
                        //errorCount++;
                        System.out.println("Tested method: " + m.getName() + " bad test.");
                        System.out.println("Please specify EXPECTED value\n");
                    } else if (functionParam.length == 2 && !functionResult.equals(parse(functionParam[1]))) {
                        failCount++;
                        System.out.println("Tested method: " + m.getName() + " test failed.");
                        System.out.println("EXPECTED: " + functionParam[1] + " GOT: " + functionResult + " TYPE: " + functionResult.getClass().getName() + "\n");
                    } else {
                        successCount++;
                        System.out.println("Tested method: " + m.getName() + " test successful.\n");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    //errorCount++;
                    System.out.println("Tested method: " + m.getName() + " bad test.");
                    System.out.println("Please specify TYPE value\n");
                } catch (NullPointerException e) {
                    //errorCount++;
                    System.out.println("Tested method: " + m.getName() + " bad test.");
                    System.out.println("Please specify one off available types: int, double, boolean, string\n");
                } catch (Exception e) {
                    errorCount++;
                    System.out.println("Tested method: " + m.getName() + " test error.\n");
                }

            }
        }
    }

    private void printBeforeExecutionTests(List<Method> methods) {
        System.out.println(new Banner().toBanner("Test Engine"));
        System.out.printf("Found %d tests in class %s.\n", methods.size(), className);
        System.out.print("Launching tests...\n\n");
        System.out.println("---BEGIN TESTING---\n");
    }

}
