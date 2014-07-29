package cm.android.app.model;

import java.util.Map;

public class TestModel extends BaseModel {
    private Test test;
    private Map<String, String> value;

    /**
     * @return the test
     */
    public Test getTest() {
        return test;
    }

    /**
     * @param test the test to set
     */
    public void setTest(Test test) {
        this.test = test;
    }

    /**
     * @return the value
     */
    public Map<String, String> getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Map<String, String> value) {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestModel [test=" + test + ", value=" + value + "]";
    }

    public static class Test {
        private String name;
        private int age;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the age
         */
        public int getAge() {
            return age;
        }

        /**
         * @param age the age to set
         */
        public void setAge(int age) {
            this.age = age;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Test [name=" + name + ", age=" + age + "]";
        }

    }
}
