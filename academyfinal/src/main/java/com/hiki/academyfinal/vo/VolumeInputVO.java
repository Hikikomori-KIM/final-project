package com.hiki.academyfinal.vo;

import java.util.List;

import com.hiki.academyfinal.dto.VolumeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolumeInputVO {
	private String volumeMl;
	private int volumeStock;
}
