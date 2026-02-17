package com.example.app1;

public final class Member {
    private final String fullName;
    private final String group;
    private final String faculty;
    private final String specialty;

    public Member(String fullName, String group, String faculty, String specialty) {
        this.fullName = fullName;
        this.group = group;
        this.faculty = faculty;
        this.specialty = specialty;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGroup() {
        return group;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getShortName() {
        String[] parts = fullName.split(" ");
        if (parts.length >= 3) {
            return parts[0] + " " + parts[1].charAt(0) + "." + parts[2].charAt(0) + ".";
        }
        return fullName;
    }
}
