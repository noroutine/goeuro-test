import suggestions.PositionSuggestion;
import suggestions.PositionSuggestionException;
import suggestions.PositionSuggestionService;

import java.util.List;

/**
 *
 * Application entry point
 *
 * See README.md for description of application
 *
 * Created by oleksii on 18/07/14.
 */
public class EntryPoint {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Must provide text as a single parameter");
            System.exit(1);
        }

        String requestString = args[0];

        // configure our service
        PositionSuggestionService service = new PositionSuggestionService();
        service.setEndpoint("http://api.goeuro.com/api/v2");

        try {
            List<PositionSuggestion> suggestions = service.getPositionSuggestions(requestString);

            String csv = service.exportAsCSV(suggestions);

            System.out.print(csv);
        } catch (PositionSuggestionException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        } finally {
            // cleanup
            service.shutdown();
        }
    }
}
