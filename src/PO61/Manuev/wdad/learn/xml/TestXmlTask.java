package PO61.Manuev.wdad.learn.xml;

import java.util.Calendar;
import java.util.TimeZone;

public class TestXmlTask {
    static final String PATH = "/Users/amanuev/IdeaProjects/javaLABS/git_1/src/PO61/Manuev/wdad/learn/xml/restaurant – right.xml";
    public static void main(String[] args) throws Exception {
        XmlTask xml = new XmlTask(PATH);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+4"));
        calendar.set(2018,9,18);
        System.out.println(xml.earningsTotal("ivanov", calendar));

    }

}