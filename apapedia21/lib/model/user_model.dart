class UserModel {
  UserModel({
    required this.id,
    required this.name,
    required this.username,
    required this.password,
    required this.email,
    required this.balance,
    required this.address,
    required this.deleted,
  });

  String id; // Assuming the id is a String, update the type accordingly
  String name;
  String username;
  String password;
  String email;
  int balance;
  String address;
  bool deleted;

  factory UserModel.fromJson(Map<String, dynamic> json) => UserModel(
        id: json["id"],
        name: json["name"],
        username: json["username"],
        password: json["password"],
        email: json["email"],
        balance: json["balance"],
        address: json["address"],
        deleted: json["deleted"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "name": name,
        "username": username,
        "password": password,
        "email": email,
        "balance": balance,
        "address": address,
        "deleted": deleted,
      };
}
