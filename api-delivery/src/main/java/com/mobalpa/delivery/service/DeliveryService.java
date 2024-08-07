// package com.mobalpa.delivery.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;

// import com.mobalpa.delivery.dto.DeliveryDTO;
// import com.mobalpa.delivery.dto.DeliveryPriceRequestDTO;
// import com.mobalpa.delivery.dto.TrackingDetailsDTO;
// import com.mobalpa.delivery.model.Delivery;
// import com.mobalpa.delivery.repository.DeliveryRepository;
// import com.mobalpa.delivery.dto.DeliveryPriceResponseDTO;

// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
// import java.util.stream.Collectors;
// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.Map;

// @Service
// public class DeliveryService {

//     @Autowired
//     private DeliveryRepository deliveryRepository;

//     @Autowired
//     private RestTemplate restTemplate;

//     @Value("${laposte.simtao.base-url}")
//     private String SIMTAO_BASE_URL;

//     @Value("${laposte.suivi.base-url}")
//     private String API_POSTE_BASE_URL;

//     @Value("${laposte.api.key}")
//     private String API_KEY;

//     private Map<String, Double> cachedPrices = new HashMap<>();

//     public Map<String, Double> getDeliveryPrices(DeliveryPriceRequestDTO requestDTO) {
//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);
//         headers.set("X-Okapi-Key", API_KEY);

//         HttpEntity<DeliveryPriceRequestDTO> request = new HttpEntity<>(requestDTO, headers);

//         ResponseEntity<DeliveryPriceResponseDTO> response = restTemplate.postForEntity(SIMTAO_BASE_URL + "/public/sto/api/v1/pricing/sto", request, DeliveryPriceResponseDTO.class);

//         if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getResult() != null) {
//             return response.getBody().getResult().getCases().stream()
//                     .flatMap(c -> c.getAccounting().stream())
//                     .flatMap(a -> a.getChargeList().stream())
//                     .collect(Collectors.toMap(DeliveryPriceResponseDTO.Result.Case.Accounting.Charge::getChargeDescription, DeliveryPriceResponseDTO.Result.Case.Accounting.Charge::getAmountWithTax));
//         }

//         throw new RuntimeException("Failed to fetch delivery prices from La Poste API");
//     }

//     public Delivery createDelivery(DeliveryDTO deliveryDTO) {
//         Delivery delivery = new Delivery();
//         delivery.setOrderId(deliveryDTO.getOrderId());
//         delivery.setAddress(deliveryDTO.getAddress());
//         delivery.setDeliveryMethod(deliveryDTO.getDeliveryMethod());
//         delivery.setStatus(Delivery.StatusType.PENDING);
//         return deliveryRepository.save(delivery);
//     }

//     public Optional<Delivery> getDeliveryById(UUID id) {
//         return deliveryRepository.findById(id);
//     }

//     public Delivery updateDeliveryStatus(UUID id, Delivery.StatusType status) {
//         Delivery delivery = deliveryRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Delivery not found"));
//         delivery.setStatus(status);
//         delivery.setUpdatedAt(LocalDateTime.now());
//         return deliveryRepository.save(delivery);
//     }

//     public List<TrackingDetailsDTO> getTrackingDetails(UUID id) {
//         Delivery delivery = deliveryRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Delivery not found"));

//         String trackingUrl = API_POSTE_BASE_URL + "/?code=" + delivery.getDeliveryMethod() + "&apikey="
//                 + "your_api_key";
//         TrackingDetailsDTO[] trackingDetails = restTemplate.getForObject(trackingUrl, TrackingDetailsDTO[].class);
//         return Arrays.asList(trackingDetails);
//     }
// }
