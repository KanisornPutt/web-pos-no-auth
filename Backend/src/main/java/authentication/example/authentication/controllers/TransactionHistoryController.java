package authentication.example.authentication.controllers;

import authentication.example.authentication.dto.TransactionHistoryDto;
import authentication.example.authentication.entities.OrderedProductEntity;
import authentication.example.authentication.entities.StoreEntity;
import authentication.example.authentication.entities.User;
import authentication.example.authentication.services.ProductService;
import authentication.example.authentication.services.TransactionHistoryService;
import authentication.example.authentication.services.UserService;
import authentication.example.authentication.services.impl.StoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;
    private final StoreServiceImpl storeService;
    private final UserService userService;
    private final ProductService productService;

    @PostMapping(path = "/{storeId}/{userId}")
    public ResponseEntity<TransactionHistoryDto> createHistory(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId,
            @RequestBody TransactionHistoryDto requestDto) {

        if (!storeService.isStaff(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Optional<StoreEntity> foundStore = storeService.findById(storeId);
        Optional<User> foundUser = userService.findUserById(userId);

        if (foundStore.isEmpty() || foundUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<OrderedProductEntity> orderedProducts = requestDto.getOrderedProducts();

        for (OrderedProductEntity product : orderedProducts) {
            productService.reduceProductStock(product.getProductId(), product.getAmount());
        }

        TransactionHistoryDto savedHistoryDto = transactionHistoryService.saveTransaction(foundStore.get(), foundUser.get(), requestDto);

        return new ResponseEntity<>(savedHistoryDto, HttpStatus.OK);
    }


    @GetMapping(path = "/{storeId}/{userId}")
    public ResponseEntity<List<TransactionHistoryDto>> getHistory(
            @PathVariable("storeId") Long storeId,
            @PathVariable("userId") Long userId) {

        if (!storeService.isStaff(userId, storeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (storeService.isAdmin(userId, storeId)) {
            List<TransactionHistoryDto> storeHistory = transactionHistoryService.findByStoreId(storeId);
            return new ResponseEntity<>(storeHistory, HttpStatus.OK);
        }

        List<TransactionHistoryDto> userInStoreHistory = transactionHistoryService.findByStoreIdAndUserID(storeId, userId);
        return new ResponseEntity<>(userInStoreHistory, HttpStatus.OK);
    }
}
