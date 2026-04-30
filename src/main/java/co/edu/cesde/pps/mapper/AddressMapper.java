package co.edu.cesde.pps.mapper;

import co.edu.cesde.pps.dto.AddressDTO;
import co.edu.cesde.pps.model.Address;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversión entre Address (Entity) y AddressDTO.
 *
 * Responsabilidades:
 * - Convertir Entity a DTO (toDTO)
 * - Convertir DTO a Entity (toEntity)
 * - Manejar null safety
 * - Extraer userId de la relación User
 */
@Component
public class AddressMapper {

    /**
     * Convierte Address Entity a AddressDTO.
     *
     * @param address Entity a convertir
     * @return AddressDTO o null si address es null
     */
    public AddressDTO toDTO(Address address) {
        if (address == null) {
            return null;
        }

        AddressDTO dto = new AddressDTO();
        dto.setAddressId(address.getAddressId());

        // Extraer userId de la relación (null-safe)
        if (address.getUser() != null) {
            dto.setUserId(address.getUser().getUserId());
        }

        dto.setType(address.getType());
        dto.setLine1(address.getLine1());
        dto.setLine2(address.getLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPostalCode(address.getPostalCode());

        // Evitar posibles NPE con Boolean
        dto.setIsDefault(address.getIsDefault() != null ? address.getIsDefault() : false);

        return dto;
    }

    /**
     * Convierte AddressDTO a Address Entity.
     *
     * NOTA:
     * - NO convierte User (se maneja en el service)
     *
     * @param dto DTO a convertir
     * @return Address Entity o null si dto es null
     */
    public Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }

        Address address = new Address();
        address.setAddressId(dto.getAddressId());

        // User se asigna en el servicio
        address.setType(dto.getType());
        address.setLine1(dto.getLine1());
        address.setLine2(dto.getLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setPostalCode(dto.getPostalCode());

        // Null safety para Boolean
        address.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false);

        return address;
    }

    /**
     * Convierte lista de Address Entities a lista de AddressDTOs.
     *
     * @param addresses Lista de entities
     * @return Lista de DTOs o lista vacía si addresses es null
     */
    public List<AddressDTO> toDTOList(List<Address> addresses) {
        if (addresses == null) {
            return new ArrayList<>();
        }

        return addresses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte lista de AddressDTOs a lista de Address Entities.
     *
     * @param dtos Lista de DTOs
     * @return Lista de entities o lista vacía si dtos es null
     */
    public List<Address> toEntityList(List<AddressDTO> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}