package com.commerceapp.domain;

public class MiVersion implements Comparable<MiVersion> {
    private int major;
    private int minor;
    private int patch;

    public MiVersion(String versionString) {
        String[] parts = versionString.split("\\.");
        if (parts.length >= 1) {
            this.major = Integer.parseInt(parts[0]);
        }
        if (parts.length >= 2) {
            this.minor = Integer.parseInt(parts[1]);
        }
        if (parts.length >= 3) {
            this.patch = Integer.parseInt(parts[2]);
        }
    }

    @Override
    public int compareTo(MiVersion other) {
        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }
        return Integer.compare(this.patch, other.patch);
    }

    // Getters and Setters
    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getPatch() {
        return patch;
    }

    public void setPatch(int patch) {
        this.patch = patch;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
