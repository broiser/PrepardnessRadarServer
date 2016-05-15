package at.jku.cis.radar.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import at.jku.cis.radar.annotations.Secured;
import at.jku.cis.radar.geojson.GeoJsonContent;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.service.FeatureEntryService;
import at.jku.cis.radar.transformer.FeatureEntryGeoJsonContentTransformer;

@Secured
@Path("featureContent")
public class FeatureContentRestService extends RestService {

    @Inject
    private FeatureEntryService featureEntryService;
    @Inject
    private FeatureEntryGeoJsonContentTransformer featureEntryGeoJsonContentTransformer;

    @GET
    @Path("{featureGroup}")
    public Response getFeatureContent(@PathParam("featureGroup") long featureGroup) {
        FeatureEntry featureEntry = featureEntryService.findByFeatureGroup(featureGroup);
        return Response.ok(buildFeatureContent(featureEntry)).build();
    }

    @POST
    public Response updateFeatureContext(GeoJsonContent geoJsonContent) {
        FeatureEntry featureEntry = updateFeatureContent(geoJsonContent);
        return Response.ok(buildFeatureContent(featureEntry)).build();
    }

    private FeatureEntry updateFeatureContent(GeoJsonContent geoJsonContent) {
        long featureGroup = geoJsonContent.getFeatureGroup();
        String description = geoJsonContent.getDescription();
        return featureEntryService.updateFeatureContent(featureGroup, geoJsonContent.getTitle(), description);
    }

    private GeoJsonContent buildFeatureContent(FeatureEntry featureEntry) {
        return featureEntryGeoJsonContentTransformer.transform(featureEntry);
    }
}
