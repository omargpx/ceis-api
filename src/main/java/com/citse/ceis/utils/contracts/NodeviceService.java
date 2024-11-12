package com.citse.ceis.utils.contracts;

import com.citse.ceis.entity.Nodevice;

public interface NodeviceService {
    Nodevice save(String dni, String eventCode);
}
