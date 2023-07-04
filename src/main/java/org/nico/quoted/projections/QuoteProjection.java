package org.nico.quoted.projections;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.springframework.data.rest.core.config.Projection;

// TODO currently unnecessary, as configured to default Spring Data Rest config
@Projection(name = "quote", types = { Quote.class })
public
interface QuoteProjection {
    long getId();
    String getText();
    Source getSource();
}
