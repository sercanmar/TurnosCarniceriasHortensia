import { Text, Pressable, PressableProps } from 'react-native';

interface Props extends PressableProps {
  label: string;
  className?: string;
}

export const ThemedButton = ({ label, className, ...rest }: Props) => {
  return (
    <Pressable
      className={`bg-red-800 py-5 px-10 rounded-2xl items-center active:bg-red-900 ${className}`}
      {...rest}
    >
      <Text className="text-white text-2xl font-bold">{label}</Text>
    </Pressable>
  );
};