package com.seazon.board.domain;

public class Festival {
    private String name;
    private String dateStart;
    private String dateEnd;
    private String location;
    private String region;
    private String category;
    private String place;
    private String status;
    private String image;

    // 생성자 (Controller에서 더미 데이터 생성 시 사용)
    public Festival(String name, String dateStart, String dateEnd, String location, String region, String category, String place, String status, String image) {
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.location = location;
        this.region = region;
        this.category = category;
        this.place = place;
        this.status = status;
        this.image = image;
    }

    // 기본 생성자 (필수)
    public Festival() {}

    // Getter와 Setter (Lombok을 사용한다면 @Getter, @Setter로 대체 가능)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDateStart() { return dateStart; }
    public void setDateStart(String dateStart) { this.dateStart = dateStart; }
    public String getDateEnd() { return dateEnd; }
    public void setDateEnd(String dateEnd) { this.dateEnd = dateEnd; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPlace() { return place; }
    public void setPlace(String Place) { this.place = place; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
