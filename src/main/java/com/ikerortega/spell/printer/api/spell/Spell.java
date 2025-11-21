package com.ikerortega.spell.printer.api.spell;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.*;


import java.util.ArrayList;
import java.util.List;


@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Spell {

    @CsvBindByName
    private String englishTitle;
    @CsvBindByName
    private String title;
    @CsvBindByName
    private String actionType;
    @CsvBindByName
    private String type;
    @CsvBindByName
    private String level;
    @CsvBindAndSplitByName(elementType = String.class, collectionType = ArrayList.class, splitOn = " ")
    private List<String> traits;
    @CsvBindAndSplitByName(elementType = String.class, collectionType = ArrayList.class, splitOn = ",")
    private List<String> traditions;
    @CsvBindByName
    private String castTime;
    @CsvBindByName
    private String muse;
    @CsvBindByName
    private String trigger;
    @CsvBindByName
    private String range;
    @CsvBindByName
    private String objectives;
    @CsvBindByName
    private String area;
    @CsvBindByName
    private String defense;
    @CsvBindByName
    private String duration;
    @CsvBindByName
    private String description;
    @CsvBindByName
    private String heightenings;

}
