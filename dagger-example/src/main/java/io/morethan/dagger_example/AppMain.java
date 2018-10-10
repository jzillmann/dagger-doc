package io.morethan.dagger_example;

public class AppMain {

    public static void main(String[] args) {
        AppComponent appComponent = DaggerAppComponent.builder()
                .port(8080)
                .build();

        System.out.println(appComponent.app());
    }
}
