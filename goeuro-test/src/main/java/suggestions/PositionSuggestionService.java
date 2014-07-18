package suggestions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import util.Mime;

import java.io.IOException;
import java.util.*;

/**
 * Operations with position suggestions
 * <p/>
 * Created by oleksii on 18/07/14.
 */
public class PositionSuggestionService {

    // @Autowired
    public String endpoint;

    /**
     * Queries remote location for suggestions and returns them
     *
     * @param query text query
     * @return list of position suggestion for query
     * @throws PositionSuggestionException if something wrong happens on the way from remote server
     */
    public List<PositionSuggestion> getPositionSuggestions(String query) throws PositionSuggestionException {
        Preconditions.checkArgument(query != null);

        final ObjectMapper jsonMapper = new ObjectMapper();

        try {
            HttpResponse<String> response = Unirest.get(endpoint + "/position/suggest/en/" + query)
                    .header("Accept", Mime.JSON.getMimeType())
                    .asString();

            return Arrays.asList(jsonMapper.readValue(response.getRawBody(), PositionSuggestion[].class));
        } catch (Exception e) {
            throw new PositionSuggestionException(e.getMessage(), e);
        }
    }

    /**
     * Exports list of suggestions as CSV with configured csvWriter
     *
     * @param positionSuggestions iterable of suggestions
     * @return CSV output as string
     */
    public String exportAsCSV(Collection<PositionSuggestion> positionSuggestions) {
        Preconditions.checkArgument(positionSuggestions != null);

        final ObjectWriter csvWriter = new CsvMapper().writer(
                CsvSchema.builder()
                        .setUseHeader(true)
                        .addColumn("_type")
                        .addColumn("_id")
                        .addColumn("name")
                        .addColumn("type")
                        .addColumn("latitude")
                        .addColumn("longitude")
                        .build());

        try {
            //noinspection ConstantConditions
            return csvWriter.writeValueAsString(Collections2.filter(Collections2.transform(positionSuggestions, new Function<PositionSuggestion, Object>() {
                @Override
                public Object apply(final PositionSuggestion positionSuggestion) {
                    try {
                        //noinspection UnusedDeclaration
                        return new Object() {
                            public String _type = positionSuggestion.get_type();
                            public long _id = positionSuggestion.get_id();
                            public String name = positionSuggestion.getName();
                            public String type = positionSuggestion.getType();
                            public double latitude = positionSuggestion.getPosition().getLatitude();
                            public double longitude = positionSuggestion.getPosition().getLongitude();
                        };
                    } catch (Exception e) {
                        // log to server log maybe, that's enough for now
                        System.err.print(e.getMessage());
                        return null; // filtered out below
                    }
                }
            }), new Predicate<Object>() {
                @Override
                public boolean apply(Object csvDto) {
                    return csvDto != null;
                }
            }));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Closes Unirest cleanly
     * Must be called explicitly on application shutdown
     */
    public void shutdown() {

        try {
            Unirest.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
