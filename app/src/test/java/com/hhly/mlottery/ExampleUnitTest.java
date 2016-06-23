package com.hhly.mlottery;


import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    //@Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);
    }

    @Test
    public void testSubString() throws Exception {
        String score = "90+";
        System.out.println(score.substring(0, score.length()-1));
    }
}