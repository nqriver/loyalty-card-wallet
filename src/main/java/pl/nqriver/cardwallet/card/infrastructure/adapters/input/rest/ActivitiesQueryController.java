package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.hateoas.assembler.ActivityModelAssembler;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.ActivityResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Api("Loyalty card activity query services")
@RestController
@RequiredArgsConstructor
public class ActivitiesQueryController {

    private final ActivityModelAssembler activityModelAssembler;
    private final ActivityFacade activityFacade;


    @ApiOperation(value = "Retrieves the window of activities of given period or all activities if no dates are given")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("card/{id}/activities")
    public CollectionModel<EntityModel<ActivityResponse>> getActivities(
            @PathVariable("id") Long loyaltyCardId,
            @RequestParam(required = false) Optional<LocalDateTime> since,
            @RequestParam(required = false) Optional<LocalDateTime> until
    ) {
        List<ActivityResponse> activities = activityFacade.getActivities(loyaltyCardId, since, until);
        return activityModelAssembler.toCollectionModel(activities)
                .add(linkTo(methodOn(ActivitiesQueryController.class).getActivities(loyaltyCardId, since, until)).withSelfRel());
    }

    @ApiOperation(value = "Retrieves the window of expenditures/outgoings of given period or all expenditures if no dates are given")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("card/{id}/activities/incomings")
    public CollectionModel<EntityModel<ActivityResponse>> getIncomings(
            @PathVariable("id") Long loyaltyCardId,
            @RequestParam(required = false) Optional<LocalDateTime> since,
            @RequestParam(required = false) Optional<LocalDateTime> until
    ) {
        List<ActivityResponse> activities = activityFacade.getIncomingActivities(loyaltyCardId, since, until);
        return activityModelAssembler.toCollectionModel(activities)
                .add(linkTo(methodOn(ActivitiesQueryController.class).getIncomings(loyaltyCardId, since, until)).withSelfRel());
    }

    @ApiOperation(value = "Retrieves the window of incomings/revenues of given period or all incomings if no dates are given")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("card/{id}/activities/outgoings")
    public CollectionModel<EntityModel<ActivityResponse>> getOutgoings(
            @PathVariable("id") Long loyaltyCardId,
            @RequestParam(required = false) Optional<LocalDateTime> since,
            @RequestParam(required = false) Optional<LocalDateTime> until
    ) {
        List<ActivityResponse> activities = activityFacade.getOutgoingActivities(loyaltyCardId, since, until);
        return activityModelAssembler.toCollectionModel(activities)
                .add(linkTo(methodOn(ActivitiesQueryController.class).getOutgoings(loyaltyCardId, since, until)).withSelfRel());
    }


}
