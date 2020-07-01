package com.bgk21.paymentsystem.rest;

import java.util.HashMap;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Singleton
@Path("conversion")
public class RSConversion {
    private final HashMap<Pair<String, String>, Double> conversions;
    
    public RSConversion() {
        conversions = new HashMap<>();
        conversions.put(new Pair("GBP", "USD"), 1.22);
        conversions.put(new Pair("GBP", "Euro"), 1.1);
        conversions.put(new Pair("GBP", "GBP"), 1.0);
        conversions.put(new Pair("USD", "GBP"), 0.82);
        conversions.put(new Pair("USD", "Euro"), 0.91);
        conversions.put(new Pair("USD", "USD"), 1.0);
        conversions.put(new Pair("Euro", "GBP"), 0.91);
        conversions.put(new Pair("Euro", "USD"), 1.1);
        conversions.put(new Pair("Euro", "Euro"), 1.0);
    }
    
    
    @GET
    @Path("/{currency1}/{currency2}")
    @Produces("application/xml, application/json")
    public Response getConversionRate(@PathParam("currency1") String currency1, @PathParam("currency2") String currency2) {
        Double conversionRate = conversions.get(new Pair(currency1, currency2));
        if(conversionRate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(conversionRate).build();
        }
    }
    
}



class Pair<L,R> {

  private final L left;
  private final R right;

  public Pair(L left, R right) {
    assert left != null;
    assert right != null;

    this.left = left;
    this.right = right;
  }

  public L getLeft() { return left; }
  public R getRight() { return right; }

  @Override
  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) return false;
    Pair pairo = (Pair) o;
    return this.left.equals(pairo.getLeft()) &&
           this.right.equals(pairo.getRight());
  }

}