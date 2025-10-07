public class ClasseSingleton {
    private static ClasseSingleton instance;

    public Integer contador;

    private ClasseSingleton (){
        this.contador = 0;
    }

    public static ClasseSingleton getInstance(){
        if (instance == null){
            synchronized (ClasseSingleton.class){
                if (instance == null){
                    instance = new ClasseSingleton();
                }
            }   
        }
        return instance;
    }

    public synchronized void incrementar(Integer valor) {
        this.contador += valor;
    }

    public synchronized Integer getContador() {
        return this.contador;
    }

    @Override
    public String toString() {
        return "ClasseSingleton [contador=" + contador + "]";
    }
}
