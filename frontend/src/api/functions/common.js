import Swal from "sweetalert2";
import { toast } from "vue3-toastify";

export const showLoading = (msg = 'Chờ xíu') => {
  Swal.fire({
    title: msg,
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

export const showPromtConfirm = (title, onConfirm) => {
  Swal.fire({
    title: title,
    showCancelButton: true,
    cancelButtonText: 'Huỷ',
    confirmButtonText: 'Đồng ý',
  }).then((result) => {
    if (result.isConfirmed) {
      onConfirm();
    }
  })
};


export const handleError = (error) => {
  const errorCode = error?.response?.data?.code;
  const defaultMessage = "Đã xảy ra lỗi không xác định! Vui lòng liên hệ bộ phận kỹ thuật.";

  const errorMessages = {
    // Common
    8888: "Vui lòng nhập đầy đủ thông tin.",
    7777: "Không cho phép tham chiếu vòng lặp.",
    9999: "Lỗi chưa được phân loại. Vui lòng liên hệ quản trị viên.",

    // User
    1001: "Người dùng đã tồn tại.",
    1002: "Không tìm thấy người dùng.",
    1003: "Email đã được sử dụng.",
    1004: "Tên đăng nhập không hợp lệ (quá ngắn).",
    1005: "Mật khẩu không hợp lệ (quá ngắn).",
    1006: "Tuổi không đủ yêu cầu.",
    1007: "Người dùng không tồn tại.",
    1008: "Mật khẩu đã được sử dụng trước đó.",
    1009: "Tạo người dùng thất bại.",
    1010: "Cập nhật người dùng thất bại.",
    1011: "Email đã tồn tại.",
    1012: "Số điện thoại đã tồn tại.",
    1013: "Email không có thay đổi.",
    1014: "Số điện thoại không có thay đổi.",
    1015: "Mật khẩu không đúng.",
    1016: "Lỗi khi tạo người dùng.",
    1017: "Lỗi khi cập nhật người dùng.",
    1019: "Mật khẩu cũ không chính xác.",
    1020: "Mật khẩu mới không được trùng với xác nhận mật khẩu.",
    1030: "Email không đúng.",

    // Permission
    1101: "Không tìm thấy quyền.",

    // Order
    1201: "Không tìm thấy đơn hàng.",
    1202: "Đơn hàng đã được xử lý.",
    1203: "Danh sách đơn hàng không tồn tại.",
    1204: "Không thể chỉnh sửa đơn hàng này.",
    1205: "Không tìm thấy trạng thái đơn hàng.",
    1206: "Đơn hàng phải ở trạng thái 'Đã giao' để thực hiện thao tác này.",
    1207: "Thời gian đổi trả hàng đã hết.",
    1208: "Yêu cầu hoàn hàng đã tồn tại.",
    1209: "Không tìm thấy thông tin hoàn đơn hàng.",
    1210: "Không tìm thấy trạng thái hoàn hàng.",
    1522: "Không thể thay đổi trạng thái đơn hàng.",

    // Address
    1301: "Không tìm thấy địa chỉ.",

    // Payment
    1401: "Không tìm thấy phương thức thanh toán.",
    1402: "Không tìm thấy trạng thái thanh toán.",

    // Product / Variant
    1501: "Không tìm thấy biến thể sản phẩm.",
    1502: "Sản phẩm đã hết hàng.",
    1503: "Không tìm thấy sản phẩm.",
    1504: "Tên sản phẩm đã tồn tại.",
    1505: "Sản phẩm đã chọn không tồn tại.",
    1506: "Biến thể sản phẩm đã tồn tại.",
    1510: "Giá trị thuộc tính biến thể đã tồn tại.",
    1511: "Chi tiết biến thể đã tồn tại.",
    1512: "SKU đã tồn tại.",
    1513: "Biến thể cập nhật đã tồn tại.",
    1514: "Không thể xóa biến thể đã tồn tại.",
    1515: "Slug sản phẩm đã tồn tại.",
    1516: "Tạo sản phẩm thất bại.",
    1517: "Cập nhật sản phẩm thất bại.",
    1518: "Xóa sản phẩm thất bại.",
    1519: "Không tìm thấy đánh giá sản phẩm.",
    1520: "Không tìm thấy thuộc tính biến thể.",
    1523: "Giá trị thuộc tính bị trùng.",
    1524: "Giá trị thuộc tính đã tồn tại.",
    1525: "ID hình ảnh không được để trống.",
    1526: "ID ảnh không được để trống.",
    1528: "Tên sản phẩm không được để trống.",
    1529: "Slug sản phẩm không được để trống.",
    1530: "Mô tả sản phẩm không được để trống.",
    1531: "Thương hiệu không được để trống.",
    1532: "Ảnh đại diện sản phẩm không được để trống.",
    1533: "Nội dung sản phẩm không được để trống.",
    1534: "Trạng thái trang chủ không được để trống.",
    1535: "Trạng thái hoạt động không được để trống.",
    1536: "Danh mục sản phẩm không được để trống.",
    1537: "Tên biến thể không được để trống.",
    1538: "Giá biến thể không được để trống.",
    1539: "Số lượng biến thể không được để trống.",
    1540: "Số lượng đã bán không được để trống.",
    1541: "Trạng thái hoạt động của biến thể không được để trống.",
    1542: "ID sản phẩm của biến thể không được để trống.",
    1543: "Giá trị thuộc tính của biến thể không được để trống.",
    1071: "Alt text không được để trống.",
    1072: "Thông số kỹ thuật không được để trống.",
    1073: "Trạng thái ảnh đại diện không được để trống.",
    1074: "Thứ tự sắp xếp ảnh không được để trống.",
    1075: "ID biến thể của ảnh không được để trống.",
    1076: "URL ảnh không được để trống.",
    1995: "Sản phẩm đã được sử dụng, không thể xóa.",

    // Cart
    1601: "Sản phẩm đã tồn tại trong giỏ hàng.",

    // File
    1701: "Tải tệp lên thất bại.",
    1702: "Xóa tệp thất bại.",
    1703: "Không tìm thấy hình ảnh.",

    // Account
    1801: "Email đã tồn tại.",
    1802: "Số điện thoại đã tồn tại.",

    // Auth
    1901: "Chưa xác thực người dùng.",
    1902: "Bạn không có quyền thực hiện hành động này.",

    // Input
    2001: "Dữ liệu nhập vào không hợp lệ.",
    2002: "Khóa thông báo không hợp lệ.",
    2003: "UserId không được để trống.",

    // Voucher
    2101: "Không tìm thấy voucher.",
    2102: "Voucher đã hết hạn.",
    2103: "Đơn hàng không đủ giá trị để áp dụng voucher.",
    2104: "Bạn đã có voucher này.",
    2105: "Voucher không hợp lệ.",
    2106: "Voucher đã vượt quá số lượt sử dụng.",

    // Category
    2201: "Danh mục không tồn tại.",
    2202: "Danh mục có danh mục con, không thể xóa.",
    2203: "Danh mục có sản phẩm, không thể xóa.",
    2204: "Tên danh mục đã tồn tại.",
    2205: "Slug danh mục đã tồn tại.",

    // System
    2501: "Không tìm thấy tài nguyên.",
  };

  const errorMessage = errorMessages[errorCode] || defaultMessage;

  toast?.error?.(errorMessage);
};
