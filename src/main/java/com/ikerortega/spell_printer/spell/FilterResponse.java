package com.ikerortega.spell_printer.spell;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterResponse {

    int size;
    List<Filter> filterList;

}
