package com.salawubabatunde.beermicroservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salawubabatunde.beermicroservice.bootstrap.BeerLoader;
import com.salawubabatunde.beermicroservice.services.BeerService;
import com.salawubabatunde.beermicroservice.web.model.BeerDto;
import com.salawubabatunde.beermicroservice.web.model.BeerStyleEnum;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.springframework.guru", uriPort = 8080)
@WebMvcTest(BeerController.class)
class BeerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    @Test
    void getBeerById() throws Exception {

        given(beerService.getById(any())).willReturn(getValidBeerDto());

     mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
              .param("iscold","yes")
             .accept(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andDo(document("v1/beer-get",
                     pathParameters(
                         parameterWithName("beerId").description("UUID of desired beer to get.")
             ),
                     queryParameters(
                             parameterWithName("iscold").description("is beer cold query param")
              ),
                     responseFields(
                             fieldWithPath("id").description("id of Beer"),
                             fieldWithPath("version").description("version number"),
                             fieldWithPath("createdDate").description("Date Created"),
                             fieldWithPath("lastModifiedDate").description("Date Updated"),
                             fieldWithPath("beerName").description("Beer Name"),
                             fieldWithPath("beerStyle").description("Beer Style"),
                             fieldWithPath("upc").description("Upc of Beer"),
                             fieldWithPath("price").description("Price"),
                             fieldWithPath("quantityOnHand").description("Quantity on hand"),
                             fieldWithPath("minOnHand").description("Minimum on hand"),
                             fieldWithPath("quantityToBrew").description("Quantity to Brew")
                     )
             ));
    }

    @Test
    void saveNewBeer() throws Exception{

        given(beerService.getById(any())).willReturn(getValidBeerDto());

        val beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        mockMvc.perform(post("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated())
                .andDo(document("v1/beer-new",
                        requestFields(
                                fields.withPath("id").ignored(),
                                fields.withPath("version").ignored(),
                                fields.withPath("createdDate").ignored(),
                                fields.withPath("lastModifiedDate").ignored(),
                                fields.withPath("beerName").description("Name of the beer"),
                                fields.withPath("beerStyle").description("Style of Beer"),
                                fields.withPath("upc").description("Beer UPC").attributes(),
                                fields.withPath("price").description("Beer price"),
                                fields.withPath("quantityOnHand").ignored(),
                                fields.withPath("minOnHand").ignored(),
                                fields.withPath("quantityToBrew").description("quantity to brew")
                        )));
    }

    @Test
    void updateBeerById() throws Exception {

        given(beerService.getById(any())).willReturn(getValidBeerDto());

        val beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);
        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isNoContent());
    }

    BeerDto getValidBeerDto(){
        return BeerDto.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("2.99"))
                .upc(BeerLoader.BEER_1_UPC)
                .build();
    }


    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }


}