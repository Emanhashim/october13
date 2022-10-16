package com.bazra.usermanagement.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MyMerchantDetails implements UserDetails {

	private MerchantInfo merchantInfo;

	private UserCredential userCredential;
	private UserAuthentication userAuthentication;
	private boolean Expired = true;
	private boolean Locked = true;;
	private boolean enabled = true;
	private boolean credentialexpired = true;

	

	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}

	

	public UserCredential getUserCredential() {
		return userCredential;
	}

	public void setUserCredential(UserCredential userCredential) {
		this.userCredential = userCredential;
	}

	public UserAuthentication getUserAuthentication() {
		return userAuthentication;
	}

	public void setUserAuthentication(UserAuthentication userAuthentication) {
		this.userAuthentication = userAuthentication;
	}

	public boolean isExpired() {
		return Expired;
	}

	public void setExpired(boolean expired) {
		Expired = expired;
	}

	public boolean isLocked() {
		return Locked;
	}

	public void setLocked(boolean locked) {
		Locked = locked;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	private String password;

	
	
	public MyMerchantDetails(MerchantInfo userInfo, UserAuthentication userAuthentication, UserCredential userCredential) {
		this.userAuthentication = userAuthentication;
		this.userCredential = userCredential;
		this.merchantInfo = userInfo;
	}
	
	

	public MyMerchantDetails(UserCredential userCredential) {
		this.userCredential = userCredential;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

			
			 if (!merchantInfo.equals(null)) {
					authorities.add(new SimpleGrantedAuthority(merchantInfo.getRoles().getRolename()));
				}


		
		return authorities;
	}

	@Override
	public String getPassword() {

		return userAuthentication.getAuthenticationValue();
	}

	@Override
	public String getUsername() {

		return userCredential.getLoginID();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return Expired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return Locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return credentialexpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

}