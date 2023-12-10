package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.Address;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.AddressRepository;
import com.thogwa.thogwa.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    public Address addAddress (Address address, String email) {
        User user = userRepository.findByEmail(email);
        Address address1 = new Address();
        if(!email.isEmpty()) {
            address1.setUsers(List.of(user));
            address1.setStreet(address.getStreet());
            address1.setCity(address.getCity());
            address1.setState(address.getState());
            address1.setCountry(address.getCountry());
            address1.setPincode(address.getPincode());
        }
        return addressRepository.save(address);
    }


}
