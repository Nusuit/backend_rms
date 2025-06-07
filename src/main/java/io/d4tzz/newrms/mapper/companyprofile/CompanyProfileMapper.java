package io.d4tzz.newrms.mapper.companyprofile;

import io.d4tzz.newrms.dto.companyprofile.CompanyProfileDto;
import io.d4tzz.newrms.entity.CompanyProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CompanyProfileMapper {

    CompanyProfileDto toCompanyProfileDto(CompanyProfile companyProfile);

    CompanyProfile toCompanyProfile(CompanyProfileDto companyProfileDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyProfileFromDto(CompanyProfileDto companyProfileDto, @MappingTarget CompanyProfile companyProfile);
} 