package com.squareapps.a4teen.amigos;

import com.squareapps.a4teen.amigos.Common.Utils.BackEndAPI;
import com.squareapps.a4teen.amigos.Common.Utils.DataModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DataModelTest {
    @Test
    public void NameCorrect() throws Exception {
        assertEquals("Robin Good", new DataModel().getName());
    }

    @Test
    public void ComsWithBackEnd() throws Exception {

        BackEndAPI api = new BackEndAPI();
        String success = api.notifyUserAdd("-KxEo678DDzdJ4pmDxsy");
        System.out.print(success);
        //assertTrue(success);
    }
}