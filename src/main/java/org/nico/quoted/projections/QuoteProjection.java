package org.nico.quoted.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;


@Projection(name = "quote", types = { Quote.class })
public
interface QuoteProjection {
    long getId();
    String getText();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Paris")
    Timestamp getDatetimeCreated();
    Source getSource();
}
