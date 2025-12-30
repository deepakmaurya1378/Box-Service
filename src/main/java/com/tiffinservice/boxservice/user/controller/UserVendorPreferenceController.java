package com.tiffinservice.boxservice.user.controller;

import com.tiffinservice.boxservice.user.dto.ChangeDefaultRequestDTO;
import com.tiffinservice.boxservice.user.dto.RemovePreferenceRequestDTO;
import com.tiffinservice.boxservice.user.dto.UserVendorPreferenceRequestDTO;
import com.tiffinservice.boxservice.user.dto.UserVendorPreferenceResponseDTO;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import com.tiffinservice.boxservice.user.service.UserVendorPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user/vendor-preferences")
@RequiredArgsConstructor
public class UserVendorPreferenceController {

    private final UserVendorPreferenceService service;

    @PostMapping("/add")
    public UserVendorPreferenceResponseDTO add(@RequestBody UserVendorPreferenceRequestDTO request) {
        return service.addVendorPreference(request);
    }

    @GetMapping("/{userId}/{shiftType}")
    public List<UserVendorPreferenceResponseDTO> getByShift(
            @PathVariable Long userId,
            @PathVariable ShiftType shiftType
    ) {
        return service.getPreferencesByShift(userId, shiftType);
    }

    @DeleteMapping("/remove")
    public String remove(@RequestBody RemovePreferenceRequestDTO request) {
        service.removeVendorPreference(request.getUserId(), request.getVendorId(), request.getShiftType());
        return "Vendor removed successfully from your " + request.getShiftType() + " preference list.";
    }

    @PutMapping("/change-default")
    public UserVendorPreferenceResponseDTO changeDefault(@RequestBody ChangeDefaultRequestDTO req) {
        return service.changeDefaultVendor(req.getUserId(), req.getVendorId(), req.getShiftType());
    }

    @PutMapping("/remove-default")
    public UserVendorPreferenceResponseDTO removeDefault(@RequestBody RemovePreferenceRequestDTO req) {
        return service.removeDefaultVendor(req.getUserId(), req.getVendorId(), req.getShiftType());
    }




}
