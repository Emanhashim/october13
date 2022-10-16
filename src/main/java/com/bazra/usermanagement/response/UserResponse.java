package com.bazra.usermanagement.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.runtime.internal.cflowstack.ThreadStackFactoryImpl;

import com.bazra.usermanagement.model.Levels;
import com.bazra.usermanagement.model.Role;
import com.bazra.usermanagement.model.Transaction;
import com.bazra.usermanagement.model.UserAddress;
import com.bazra.usermanagement.model.UserInfo;

import net.bytebuddy.asm.Advice.This;

public class UserResponse {
	private String message;
	private Integer user_id;
	private String username;
  
    private String birthDay;
	
	private String email;
	private String middlename;
	private String lastName;
    private String firstName;
    private String motherName;
 
    private String gender;
    private Role roles;
	private Levels levels;
	private Boolean locked;
    private String region;
    private String city;
    private String houseNo;
	private String subCity;
	private String woreda;
	private String userType;
	
    private String photo;
    private String kebeleID;
    private String idType;
    private String idNumber;
    private List<UserInfo> userInfos = new ArrayList<>();
//    private UserInfo userInfo = new UserInfo();
	
	public long getUser_id() {
		return user_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public List<UserInfo> getUserInfos() {
		return userInfos;
	}
	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Levels getLevels() {
		return levels;
	}
	public void setLevels(Levels levels) {
		this.levels = levels;
	}
	public Boolean getLocked() {
		return locked;
	}
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Role getRoles() {
		return roles;
	}
	public void setRoles(Role roles) {
		this.roles = roles;
	}
	public String getSubCity() {
		return subCity;
	}
	public void setSubCity(String subCity) {
		this.subCity = subCity;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getWoreda() {
		return woreda;
	}
	public void setWoreda(String woreda) {
		this.woreda = woreda;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getKebeleID() {
		return kebeleID;
	}
	public void setKebeleID(String kebeleID) {
		this.kebeleID = kebeleID;
	}
    
    public UserResponse(List<UserInfo> userInfoss) {
    	
    	this.userInfos
        = new ArrayList<>(userInfoss);
    	
    }
    
    public UserResponse(UserInfo userInfoss,List<UserAddress> useraddressOptional) {

    	this.user_id=userInfoss.getId();
    	this.birthDay=userInfoss.getBirthDay();
    	this.email=userInfoss.getEmail();
    	this.firstName=userInfoss.getName();
    	this.lastName=userInfoss.getLastname();
    	this.middlename=userInfoss.getFatherName();
    	this.motherName=userInfoss.getMotherName();
    	this.gender=userInfoss.getGender();
    	this.levels=userInfoss.getLevels();
    	this.roles=userInfoss.getRoles();
    	this.username=userInfoss.getUsername();
    	
    	for (int i = 0; i < useraddressOptional.size(); i++) {
			if (useraddressOptional.get(i).getAddressElement().getTitle().matches("REGION")) {
				this.region=useraddressOptional.get(i).getAddress();
			}
			else if (useraddressOptional.get(i).getAddressElement().getTitle().matches("CITY")) {
				this.city=useraddressOptional.get(i).getAddress();
			}
			else if (useraddressOptional.get(i).getAddressElement().getTitle().matches("SUB CITY")) {
				this.subCity=useraddressOptional.get(i).getAddress();
			}
			else if (useraddressOptional.get(i).getAddressElement().getTitle().matches("HOUSE NUMBER")) {
				this.houseNo=useraddressOptional.get(i).getAddress();
			}
			else if (useraddressOptional.get(i).getAddressElement().getTitle().matches("WOREDA")) {
				this.woreda=useraddressOptional.get(i).getAddress();
			}
			
		}
    
    }
   
    
}
