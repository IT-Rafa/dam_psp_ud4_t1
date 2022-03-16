package es.itrafa.dam_psp_ud4_t1_act2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author it-ra
 */
public class LogFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        SimpleDateFormat DateFor = new SimpleDateFormat("HH:mm:ss: dd/MMMM/yyyy");
        return DateFor.format(new Date()) + ": "
                + record.getSourceClassName() + ": "
                + record.getSourceMethodName() + ":\n\t"
                + record.getLevel() + ": "
                + record.getMessage() + "\n";
    }

}
