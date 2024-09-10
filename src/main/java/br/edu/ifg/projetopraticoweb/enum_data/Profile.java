package br.edu.ifg.projetopraticoweb.enum_data;

public enum Profile {
    ADMIN("ADMIN"),
    USER("USER"),
    SUPERVISOR("SUPER");

    private final String name;

    Profile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
