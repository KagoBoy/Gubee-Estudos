public class BoxingExample {

    public static void main(String[] args) {

        Integer ageObj = null;

        // Erro → NullPointerException por unboxing
        // int age = ageObj;

        int age = ageObj != null ? ageObj : 0;

        // autoboxing
        Integer x = 10; // transforma o tipo int 10 em Object Integer

        // unboxing
        int y = x; // transforma o tipo Object Integer em primitivo int

        System.out.println(age);
        System.out.println(y);
    }
}