export type ToastOptions = {
    title: string;
    message: string;
    type: "info" | "success" | "warning" | "error";
    duration?: number;
};

// TODO: Implement a real toast
export function displayToast(options: ToastOptions) {
    alert(options.message);
}
