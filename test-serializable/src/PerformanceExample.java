public class PerformanceExample {

    public static void main(String[] args) {

        int sumPrimitive = sumPrimitives();

//        for (int i = 0; i < 1_000_000; i++) {
//            sumPrimitive += i;
//        }

        Integer sumObject = sumObjects();

//        for (int i = 0; i < 1_000_000; i++) {
//            sumObject += i; // boxing + unboxing em toda iteração
//        }

        System.out.println(sumPrimitive);
        System.out.println(sumObject);
    }

    public static int sumPrimitives() {
        int sum = 0;
        for (int i = 0; i < 1_000_000; i++) {
            sum += i;
        }
        return sum;
    }

    public static int sumObjects() {
        Integer sum = 0;
        for (int i = 0; i < 1_000_000; i++) {
            sum += i;
        }
        return sum;
    }
}