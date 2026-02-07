package com.ikerortega.spell_printer.spell;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ikerortega.spell_printer.util.CSVReaderUtil;
import com.ikerortega.spell_printer.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/spell")
public class SpellApi {

    @Value("${pathfinder2e.spell.csv.path}")
    private String SPELLS_CSV_PATH;

    private final ObjectWriter ow = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).writer().withDefaultPrettyPrinter();
    private final Logger logger = LoggerFactory.getLogger(SpellApi.class);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public String getSpellByTitle(@RequestParam(required = false, defaultValue = "") String q,
                                  @RequestParam(required = false, defaultValue = "") List<String> range) {
        String output = "";
        try {
            List<Spell> spellList = CSVReaderUtil.readCSV(SPELLS_CSV_PATH, Spell.class, q);
            if(range != null && !range.isEmpty()){
                spellList = spellList.stream().filter(spell -> range.contains(spell.getRange())).toList();
            }

            SpellResponse spellResponse = new SpellResponse();
            spellResponse.filter = q;
            spellResponse.spellList = spellList;
            spellResponse.size = spellList.size();
            output = ow.writeValueAsString(spellResponse);

        } catch (Exception e) {
            logger.error("Error retrieving spells: ", e);
        }
        return output;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filters")
    public String getFilters() {
        String output = "";
        Map<String, List<String>> spellList = CSVReaderUtil.readCSVToMap(SPELLS_CSV_PATH, null);

        try {
            List<Filter> filterList = new ArrayList<>();
            for(String filterColumn: Constants.FILTER_COLUMNS){
                if(spellList.containsKey(filterColumn)){
                    Filter filter = new Filter();
                    filter.setTitle(filterColumn);
                    List<String> columnValues = new ArrayList<>();
                    List<String> filterOptionList;
                    switch (filterColumn){
                        case "traditions":
                        case "traits":
                            for(String value: spellList.get(filterColumn)){
                                columnValues.addAll(List.of(value.split(",")));
                            }
                        break;
                        default:
                            columnValues = spellList.get(filterColumn);
                    }

                    filterOptionList = columnValues.stream().distinct().sorted().toList();
                    filter.setOptions(filterOptionList);
                    filterList.add(filter);
                }
            }

            FilterResponse filterResponse = new FilterResponse(filterList.size(), filterList);
            output = ow.writeValueAsString(filterResponse);
        } catch (Exception e) {
            logger.error("Error retrieving filters: ", e);
        }
        return output;
    }
}
