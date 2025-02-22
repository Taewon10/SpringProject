package user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import user.bean.UserUploadDTO;

@Mapper
public interface UserUploadDAO {

	public void upload(List<UserUploadDTO> imageUploadList);

	public List<UserUploadDTO> uploadList();

	public UserUploadDTO getUploadDTO(String seq);

	public String getImageFileName(int seq);

	public void uploadUpdate(UserUploadDTO userUploadDTO);

	public void uploadDelete(List<String> list);

}
