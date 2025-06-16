import Swal from "sweetalert2";
import { toast } from "vue3-toastify";

export const showLoading = () => {
  Swal.fire({
    title: "Chờ xíu...",
    didOpen: () => {
      Swal.showLoading();
    },
  });
};
export const hideLoading = () => {
  Swal.close();
};

export const showPromtDelete = (onConfirm) => {
  Swal.fire({
    title: "Bạn có chắc chắn muốn xoá?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Đồng ý",
    cancelButtonText: "Huỷ",
  }).then((result) => {
    if (result.isConfirmed) {
      onConfirm();
    }
  });
};

export const handleError = (error) => {
  console.log(error);
  const errorCode = error?.response?.data?.code;
  let errorMessage = "Đã xảy ra lỗi. Vui lòng thử lại.";

  switch (errorCode) {
    // Common
    case 8888:
      errorMessage = "Vui lòng nhập đầy đủ thông tin.";
      break;
    case 7777:
      errorMessage = "Không cho phép tham chiếu vòng lặp.";
      break;
    case 9999:
      errorMessage = "Lỗi chưa được phân loại. Vui lòng liên hệ quản trị viên.";
      break;

    // User
    case 1001:
      errorMessage = "Người dùng đã tồn tại.";
      break;
    case 1002:
      errorMessage = "Không tìm thấy người dùng.";
      break;
    case 1003:
      errorMessage = "Email đã được sử dụng.";
      break;
    case 1004:
      errorMessage = "Tên đăng nhập không hợp lệ (quá ngắn).";
      break;
    case 1005:
      errorMessage = "Mật khẩu không hợp lệ (quá ngắn).";
      break;
    case 1006:
      errorMessage = "Người dùng không tồn tại hoặc mật khẩu không đúng.";
      break;
    case 1007:
      errorMessage = "Mật khẩu đã được sử dụng trước đó.";
      break;
    case 1008:
      errorMessage = "Tạo người dùng thất bại.";
      break;
    case 1009:
      errorMessage = "Cập nhật người dùng thất bại.";
      break;
    case 1010:
      errorMessage = "Email đã tồn tại.";
      break;
    case 1011:
      errorMessage = "Số điện thoại đã tồn tại.";
      break;
    case 1017:
      errorMessage = "Lỗi khi tạo người dùng.";
      break;
    case 1018:
      errorMessage = "Lỗi khi cập nhật người dùng hoặc tải ảnh.";
      break;

    // Product
    case 1026:
      errorMessage = "Slug sản phẩm đã tồn tại.";
      break;
    case 1027:
      errorMessage = "Sản phẩm cần tạo đã tồn tại.";
      break;
    case 1028:
      errorMessage = "Sản phẩm cần cập nhật không tồn tại.";
      break;
    case 1029:
      errorMessage = "Sản phẩm cần xóa không tồn tại.";
      break;
    case 1030:
      errorMessage = "Không tìm thấy đánh giá sản phẩm.";
      break;

    // Permission
    case 1101:
      errorMessage = "Không tìm thấy quyền.";
      break;

    // Order
    case 1201:
      errorMessage = "Không tìm thấy đơn hàng.";
      break;
    case 1202:
      errorMessage = "Đơn hàng đã được xử lý.";
      break;
    case 1203:
      errorMessage = "Danh sách đơn hàng không tồn tại.";
      break;
    case 1204:
      errorMessage = "Không thể chỉnh sửa đơn hàng này.";
      break;
    case 1205:
      errorMessage = "Không tìm thấy trạng thái đơn hàng.";
      break;
    case 1206:
      errorMessage = "Đơn hàng phải ở trạng thái 'Đã giao' để thực hiện thao tác này.";
      break;
    case 1207:
      errorMessage = "Thời gian đổi trả hàng (7 ngày) đã hết.";
      break;
    case 1208:
      errorMessage = "Yêu cầu hoàn hàng đã tồn tại.";
      break;
    case 1209:
      errorMessage = "Không tìm thấy thông tin hoàn đơn hàng.";
      break;
    case 1210:
      errorMessage = "Không tìm thấy trạng thái hoàn hàng.";
      break;

    // Address
    case 1301:
      errorMessage = "Không tìm thấy địa chỉ.";
      break;

    // Payment
    case 1401:
      errorMessage = "Không tìm thấy phương thức hoặc trạng thái thanh toán.";
      break;

    // Product Variant
    case 1501:
      errorMessage = "Không tìm thấy biến thể sản phẩm.";
      break;
    case 1502:
      errorMessage = "Sản phẩm đã hết hàng.";
      break;
    case 1503:
      errorMessage = "Không tìm thấy sản phẩm.";
      break;
    case 1504:
      errorMessage = "Sản phẩm đã chọn không tồn tại.";
      break;
    case 1239:
    case 1248:
      errorMessage = "SKU đã tồn tại.";
      break;
    case 1240:
      errorMessage = "Chi tiết ảnh sản phẩm đã tồn tại.";
      break;
    case 1242:
      errorMessage = "Không thể cập nhật ảnh sản phẩm.";
      break;
    case 1243:
      errorMessage = "Không thể xóa ảnh sản phẩm.";
      break;
    case 1244:
      errorMessage = "Chi tiết biến thể đã tồn tại.";
      break;
    case 1245:
      errorMessage = "Không thể cập nhật biến thể.";
      break;
    case 1246:
      errorMessage = "Không thể xóa biến thể.";
      break;
    case 1247:
      errorMessage = "Giá trị biến thể đã tồn tại.";
      break;
    case 1238:
      errorMessage = "Không thể xóa biến thể sản phẩm.";
      break;

    // Cart
    case 1601:
      errorMessage = "Sản phẩm đã tồn tại trong giỏ hàng.";
      break;

    // File
    case 1701:
      errorMessage = "Tải tệp lên thất bại.";
      break;
    case 1702:
      errorMessage = "Xóa tệp thất bại.";
      break;
    case 1703:
      errorMessage = "Không tìm thấy hình ảnh.";
      break;

    // Account
    case 1801:
      errorMessage = "Email đã tồn tại.";
      break;
    case 1802:
      errorMessage = "Số điện thoại đã tồn tại.";
      break;

    // Auth
    case 1901:
      errorMessage = "Chưa xác thực người dùng.";
      break;
    case 1902:
      errorMessage = "Bạn không có quyền thực hiện hành động này.";
      break;

    // Input
    case 2001:
      errorMessage = "Dữ liệu nhập vào không hợp lệ.";
      break;
    case 2002:
      errorMessage = "Khóa thông báo không hợp lệ.";
      break;

    // Voucher
    case 2101:
      errorMessage = "Không tìm thấy voucher.";
      break;
    case 2102:
      errorMessage = "Voucher đã hết hạn.";
      break;
    case 2103:
      errorMessage = "Giá trị đơn hàng chưa đủ để áp dụng voucher.";
      break;
    case 2104:
      errorMessage = "Bạn đã có voucher này rồi.";
      break;
    case 2105:
      errorMessage = "Voucher không hợp lệ.";
      break;
    case 2106:
      errorMessage = "Voucher đã vượt quá số lượt sử dụng.";
      break;

    // Category
    case 2201:
      errorMessage = "Danh mục không tồn tại.";
      break;
    case 2202:
      errorMessage = "Danh mục có danh mục con, không thể xóa.";
      break;
    case 2203:
      errorMessage = "Danh mục có sản phẩm, không thể xóa.";
      break;
    case 2204:
      errorMessage = "Tên danh mục đã tồn tại.";
      break;
    case 2205:
      errorMessage = "Slug của danh mục đã tồn tại.";
      break;

    // System
    case 2501:
      errorMessage = "Không tìm thấy tài nguyên.";
      break;

    default:
      errorMessage = "Lỗi không xác định! Vui lòng liên hệ bộ phận kỹ thuật.";
      break;
  }

  toast.error(errorMessage);
};
