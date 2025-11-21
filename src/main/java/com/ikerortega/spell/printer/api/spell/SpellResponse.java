package com.ikerortega.spell.printer.api.spell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpellResponse {
    int size;
    String filter;
    List<Spell> spellList;

}
