package br.edu.ifg.projetopraticoweb.enum_data;

import lombok.Getter;

@Getter
public enum Profile {
    ADMIN("ADMIN"),
    USER("USER"),
    SUPERVISOR("SUPERVISOR");

    private final String name;

    Profile(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
