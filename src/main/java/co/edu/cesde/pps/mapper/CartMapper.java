package co.edu.cesde.pps.mapper;

import co.edu.cesde.pps.dto.CartDTO;
import co.edu.cesde.pps.dto.CartItemDTO;
import co.edu.cesde.pps.model.Cart;
import co.edu.cesde.pps.model.CartItem;
import co.edu.cesde.pps.util.MoneyUtils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversión entre Cart/CartItem (Entities) y CartDTO/CartItemDTO.
 */
@Component
public class CartMapper {

    /**
     * Convierte Cart Entity a CartDTO.
     */
    public CartDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());

        // Relación User (null-safe)
        if (cart.getUser() != null) {
            dto.setUserId(cart.getUser().getUserId());
            dto.setUserEmail(cart.getUser().getEmail());
        }

        dto.setStatus(cart.getStatus());

        // Evitar NPE en helper
        dto.setIsGuest(cart.isGuestCart());

        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        // Items (null-safe + lista mutable)
        if (cart.getItems() != null) {
            List<CartItemDTO> itemDTOs = cart.getItems().stream()
                    .map(this::toCartItemDTO)
                    .collect(Collectors.toList());

            dto.setItems(itemDTOs);
            dto.setItemsCount(itemDTOs.size());
        } else {
            dto.setItems(new ArrayList<>());
            dto.setItemsCount(0);
        }

        // Total (protegido contra nulls)
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            dto.setTotal(cart.calculateTotal());
        } else {
            dto.setTotal(null);
        }

        // Formateo seguro
        if (dto.getTotal() != null) {
            dto.setTotalFormatted(MoneyUtils.formatUSD(dto.getTotal()));
        }

        return dto;
    }

    /**
     * Convierte CartItem Entity a CartItemDTO.
     */
    public CartItemDTO toCartItemDTO(CartItem item) {
        if (item == null) {
            return null;
        }

        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(item.getCartItemId());

        if (item.getCart() != null) {
            dto.setCartId(item.getCart().getCartId());
        }

        // Relación Product (null-safe)
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getProductId());
            dto.setProductName(item.getProduct().getName());
            dto.setProductSku(item.getProduct().getSku());
            dto.setProductAvailable(item.getProduct().isAvailable());
            dto.setProductStock(item.getProduct().getStockQty());
        }

        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setAddedAt(item.getAddedAt());

        // Subtotal protegido
        if (item.getQuantity() != null && item.getUnitPrice() != null) {
            dto.setSubtotal(item.calculateSubtotal());
        } else {
            dto.setSubtotal(null);
        }

        // Formateo seguro
        if (dto.getUnitPrice() != null) {
            dto.setUnitPriceFormatted(MoneyUtils.formatUSD(dto.getUnitPrice()));
        }

        if (dto.getSubtotal() != null) {
            dto.setSubtotalFormatted(MoneyUtils.formatUSD(dto.getSubtotal()));
        }

        return dto;
    }

    /**
     * Convierte CartDTO a Cart Entity.
     */
    public Cart toEntity(CartDTO dto) {
        if (dto == null) {
            return null;
        }

        Cart cart = new Cart();
        cart.setCartId(dto.getCartId());

        // User se asigna en el service
        cart.setStatus(dto.getStatus());

        // ❌ Evitar sobrescribir fechas del sistema
        // cart.setCreatedAt(dto.getCreatedAt());
        // cart.setUpdatedAt(dto.getUpdatedAt());

        return cart;
    }

    /**
     * Convierte CartItemDTO a CartItem Entity.
     */
    public CartItem toCartItemEntity(CartItemDTO dto) {
        if (dto == null) {
            return null;
        }

        CartItem item = new CartItem();
        item.setCartItemId(dto.getCartItemId());

        // Cart y Product se asignan en el service
        item.setQuantity(dto.getQuantity());
        item.setUnitPrice(dto.getUnitPrice());
        item.setAddedAt(dto.getAddedAt());

        return item;
    }

    /**
     * Lista Cart → DTO
     */
    public List<CartDTO> toDTOList(List<Cart> carts) {
        if (carts == null) {
            return new ArrayList<>();
        }

        return carts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista DTO → Cart
     */
    public List<Cart> toEntityList(List<CartDTO> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}