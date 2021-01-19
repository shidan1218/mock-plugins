package com.shidan.ark.mock.core.plugins;

public class MockType {

    public static MockType HTTP = new MockType("http");

    public static MockType JAVA = new MockType("java");

    public static MockType DUBBO = new MockType("dubbo");


    private String name;

    public MockType(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MockType that = (MockType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    @Override
    public String toString() {
        return "MockType{" +
                "name='" + name + '\'' +
                '}';
    }
}
