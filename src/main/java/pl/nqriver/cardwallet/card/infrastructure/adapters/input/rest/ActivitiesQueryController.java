package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.ActivityResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class ActivitiesQueryController {

    private final ActivityModelAssembler activityModelAssembler;
    private final ActivityFacade activityFacade;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("card/{id}/activities")
    CollectionModel<EntityModel<ActivityResponse>> getActivities(
            @PathVariable("id") Long loyaltyCardId,
            @RequestParam(required = false) Optional<LocalDateTime> since,
            @RequestParam(required = false) Optional<LocalDateTime> until
    ) {
        List<ActivityResponse> activities = activityFacade.getActivities(loyaltyCardId, since, until);
        return activityModelAssembler.toCollectionModel(activities)
                .add(linkTo(methodOn(ActivitiesQueryController.class).getActivities(loyaltyCardId, since, until)).withSelfRel());
    }


}
